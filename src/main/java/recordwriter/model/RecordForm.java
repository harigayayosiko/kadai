package recordwriter.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by KAJIWARAYutaka on 2015/05/17.
 */
@Data
public class RecordForm {
    @NotNull
    @Size(min = 1,max=70)
    private String title;

    @NotNull
    @Size(min = 1,max=140)
    private String record;
}
