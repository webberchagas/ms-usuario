package br.com.webberchagas.ms_usuario.controller;

import br.com.webberchagas.ms_usuario.business.UsuarioService;
import br.com.webberchagas.ms_usuario.business.dtos.EnderecoDTO;
import br.com.webberchagas.ms_usuario.business.dtos.TelefoneDTO;
import br.com.webberchagas.ms_usuario.business.dtos.UsuarioDTO;
import br.com.webberchagas.ms_usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> registerNewUser(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCreated = usuarioService.salvar(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreated);
    }

    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody UsuarioDTO usuarioDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuarioDto.getEmail(),
                        usuarioDto.getSenha()
                )
        );

        return ResponseEntity.ok("Bearer " + jwtUtil.generateToken(authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorEmail(@RequestParam String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deletarUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletarUsuarioPorEmail(email);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> atualizarUsuarioPorEmail(@RequestBody UsuarioDTO usuarioDTO,
                                                               @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(usuarioService.atualizaDadosUsuario(token, usuarioDTO));
    }

    @PutMapping("/endereco")
    public ResponseEntity<EnderecoDTO> atualizaEnderecoPorId(@RequestParam("id") Long id,
                                                              @RequestBody EnderecoDTO enderecoDTO){
        return ResponseEntity.ok(usuarioService.atualizaEndereco(id,enderecoDTO));
    }

    @PutMapping("/telefone")
    public ResponseEntity<TelefoneDTO> atualizaTelefonePorId(@RequestParam("id") Long id,
                                                             @RequestBody TelefoneDTO telefoneDTO){
        return ResponseEntity.ok(usuarioService.atualizaTelefone(id,telefoneDTO));
    }

    @PostMapping("/endereco")
    public ResponseEntity<EnderecoDTO> cadastraEndereco(@RequestBody EnderecoDTO enderecoDTO,
                                                        @RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastraEndereco(token,enderecoDTO));
    }

    @PostMapping("/telefone")
    public ResponseEntity<TelefoneDTO> cadastraTelefone(@RequestBody TelefoneDTO telefoneDTO,
                                                        @RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastraTelefone(token,telefoneDTO));
    }
}
