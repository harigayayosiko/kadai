package recordwriter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recordwriter.model.Record;

/**
 * Created by KAJIWARAYutaka on 2015/05/17.
 */
public interface RecordRepository extends JpaRepository<Record,Integer> {
}
