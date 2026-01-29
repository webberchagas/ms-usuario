package br.com.webberchagas.ms_usuario.business.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TelefoneDTO {

    private Long id;
    private String numero;
    private String ddd;

}
