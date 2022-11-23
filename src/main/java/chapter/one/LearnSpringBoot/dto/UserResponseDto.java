package chapter.one.LearnSpringBoot.dto;

import chapter.one.LearnSpringBoot.entities.User;
import chapter.one.LearnSpringBoot.exceptions.BaseCustomException;
import lombok.Data;

@Data
public class UserResponseDto extends BaseResponseDto {
    private User data; // actual data transported

    public UserResponseDto(User data, boolean success) {
        super();
        this.data = data;
        super.setSuccess(success);
    }

    public UserResponseDto(BaseCustomException exception) {
        super.setError(exception);
    }
}
