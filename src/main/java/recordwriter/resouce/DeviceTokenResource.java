package recordwriter.resouce;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitosync.model.Platform;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import recordwriter.Repository.DeviceTokenRepository;
import recordwriter.model.DeviceToken;
import recordwriter.model.MessageForm;
import recordwriter.tool.SampleMessageGenerator;
import recordwriter.tool.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KAJIWARAYutaka on 2015/07/05.
 */
@RestController
@RequestMapping("/api")
public class DeviceTokenResource {
    @Autowired
    DeviceTokenRepository deviceTokenRepository;

    DeviceTokenResource(){
        snsClient = new AmazonSNSClient();
    }
    AmazonSNS snsClient;

    private static final String applicationName = "SendSample";

    private static final Logger logger = LoggerFactory.getLogger(DeviceTokenResource.class);
    @RequestMapping(value="deviceRegister",method= RequestMethod.PUT)
    public Boolean registerDeviceToken(@RequestBody DeviceToken deviceToken){
        List<DeviceToken> deviceTokenList = deviceTokenRepository.findByDeviceToken(deviceToken.getDeviceToken());
        if(deviceTokenList.size() < 1){
            deviceTokenRepository.save(deviceToken);
            AmazonSNSClient client = new AmazonSNSClient();
            ListEndpointsByPlatformApplicationRequest listEndpointsByPlatformApplicationRequest = new ListEndpointsByPlatformApplicationRequest();
            CreatePlatformEndpointRequest platformEndpointRequest = new CreatePlatformEndpointRequest();
            client.setRegion(Region.getRegion(Regions.US_EAST_1));
            client.listPlatformApplications().getPlatformApplications().stream()
                    .filter(e -> e.getPlatformApplicationArn().matches("^.*" + applicationName + "$"))
                    .forEach(platformApplication -> {
                        if(snsClient.listEndpointsByPlatformApplication(listEndpointsByPlatformApplicationRequest.withPlatformApplicationArn(platformApplication.getPlatformApplicationArn())).getEndpoints()
                                        .stream().filter(endpoint -> endpoint.getAttributes().get("Token").equals(deviceToken.getDeviceToken())).count() < 1){
                            platformEndpointRequest.setToken(deviceToken.getDeviceToken());
                            platformEndpointRequest.setPlatformApplicationArn(platformApplication.getPlatformApplicationArn());
                            client.createPlatformEndpoint(platformEndpointRequest);
                        }
                    });
        }
        return true;
    }

    @RequestMapping(value="sendMessage",method = RequestMethod.POST)
    public void sendMessage(@RequestBody MessageForm messageForm){
        AmazonSNSClient client = new AmazonSNSClient();
        PublishRequest publishRequest = new PublishRequest();
        publishRequest.setMessage(messageForm.getMessage());
        ListEndpointsByPlatformApplicationRequest listEndpointsByPlatformApplicationRequest = new ListEndpointsByPlatformApplicationRequest();
        client.setRegion(Region.getRegion(Regions.US_EAST_1));
        client.listPlatformApplications().getPlatformApplications().stream()
                .filter(e -> e.getPlatformApplicationArn().matches("^.*" + applicationName + "$"))
                .forEach(platformApplication -> {
                    listEndpointsByPlatformApplicationRequest.withPlatformApplicationArn(platformApplication.getPlatformApplicationArn());
                    snsClient.listEndpointsByPlatformApplication(listEndpointsByPlatformApplicationRequest)
                            .getEndpoints().stream()
                            .filter(endpoint -> endpoint.getAttributes().get("Enabled").equals("true"))
                            .forEach(endpoint -> {
                                publishRequest.setTargetArn(endpoint.getEndpointArn());
                                client.publish(publishRequest);
                            });
                });
    }

}
