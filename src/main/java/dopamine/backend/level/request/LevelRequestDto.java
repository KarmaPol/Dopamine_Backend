package dopamine.backend.level.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LevelRequestDto {

    // todo : 입력을 안하면 0으로 되는 현상
    @NotNull(message = "Level은 필수로 입력해야 합니다.")
    private int levelNum;

    private String name;

    private String image;

    private int challengeCnt;
}