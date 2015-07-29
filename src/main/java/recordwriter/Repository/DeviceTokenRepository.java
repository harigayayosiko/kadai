package recordwriter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recordwriter.model.DeviceToken;

import java.util.List;

/**
 * Created by KAJIWARAYutaka on 2015/07/05.
 */
public interface DeviceTokenRepository extends JpaRepository<DeviceToken,Integer> {
    List<DeviceToken> findByDeviceToken(String deviceToken);
}
