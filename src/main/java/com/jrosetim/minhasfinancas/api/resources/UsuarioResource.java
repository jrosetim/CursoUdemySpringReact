package com.jrosetim.minhasfinancas.api.resources;

import com.jrosetim.minhasfinancas.api.dto.UsuarioDTO;
import com.jrosetim.minhasfinancas.exception.AutenticacaoException;
import com.jrosetim.minhasfinancas.exception.RegraNegocioException;
import com.jrosetim.minhasfinancas.model.UsuarioModel;
import com.jrosetim.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){
        UsuarioModel usuario = UsuarioModel
                                .builder()
                                .nome(usuarioDTO.getNome())
                                .email(usuarioDTO.getEmail())
                                .senha(usuarioDTO.getSenha())
                                .build();

        try {
            UsuarioModel usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return ResponseEntity.ok().body(usuarioSalvo);
        }catch(RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO){
        try {
            UsuarioModel usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        }catch (AutenticacaoException e){
            ResponseEntity.badRequest().body(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
