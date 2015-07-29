package recordwriter.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by KAJIWARAYutaka on 2015/07/05.
 */
@Data
public class MessageForm {
    @NotNull
    @Size(min = 1,max=140)
    private String message;
}
