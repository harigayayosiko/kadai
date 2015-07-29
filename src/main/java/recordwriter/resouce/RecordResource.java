package recordwriter.resouce;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import recordwriter.Repository.RecordRepository;
import recordwriter.model.Record;

import java.util.List;
import java.util.Map;

/**
 * Created by kajiwarayutaka on 2015/06/02.
 */
@RestController
@RequestMapping("/api")
public class RecordResource {
    @Autowired
    RecordRepository recordRepository;

    @RequestMapping(value="record/create",method = RequestMethod.POST)
    public ResponseEntity<String> createRecord(@RequestBody Record record,BindingResult result){
        HttpHeaders headers = new HttpHeaders();
        if(result.hasErrors()){
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>("text content", headers, status);
        }else{
            recordRepository.save(record);
            HttpStatus status = HttpStatus.CREATED;
            return new ResponseEntity<>("", headers, status);
        }
    }

    @RequestMapping(value="records",method= RequestMethod.GET)
    public List<Record> getRecords(){
        return recordRepository.findAll();
    }

    @RequestMapping(value="record/{id}",method = RequestMethod.GET)
    public Record getRecord(@PathVariable(value = "id") Integer id){
        return recordRepository.findOne(id);
    }

    @RequestMapping(value="record/{id}",method = RequestMethod.PUT)
    public HttpStatus updateRecord(@PathVariable(value = "id") Integer id,@RequestBody Record record,BindingResult result){
        if(result.hasErrors()){
            return HttpStatus.BAD_REQUEST;
        }else{
            recordRepository.save(record);
            return HttpStatus.OK;
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value="record/{id}",method = RequestMethod.DELETE)
    public void deleteRecord(@PathVariable(value = "id") Integer id){
        recordRepository.delete(id);
    }
}
