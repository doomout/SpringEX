package org.zerock.springex.dto;

import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {
    private Long tno;
    
    @NotEmpty //null, 빈 문자열 불가
    private String title;
    
    @Future //현재 보다 미래인가? 체크
    private LocalDate dueDate;

    private boolean finished;
    
    @NotEmpty //null, 빈 문자열 불가
    private String writer;
}
