package ma.formations.jpa.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class PersonneDto {
    private Long id;
    private String firstname;
    private String lastname;
    private List<ActiviteDto> activites=new ArrayList<>();
}
