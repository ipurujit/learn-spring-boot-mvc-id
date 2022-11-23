package chapter.one.LearnSpringBoot.dto;

import chapter.one.LearnSpringBoot.entities.User;
import chapter.one.LearnSpringBoot.exceptions.BaseCustomException;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BaseResponseDto {
    private boolean success = false;
    private Object data; // actual data transported

    @JsonInclude(JsonInclude.Include.NON_NULL) // Do not return errorCode when null
    private Integer errorCode; // 0 for success - standard unix cmd behaviour, >=1 for errors

    @JsonInclude(JsonInclude.Include.NON_NULL) // Do not return errorMsg when null
    private String errorMsg;

    public BaseResponseDto(BaseCustomException exception) {
        this.setError(exception);
    }

    public void setError(BaseCustomException exception) {
        this.errorCode = exception.getErrorCode();
        this.errorMsg = exception.getErrorMsg();
    }
}
