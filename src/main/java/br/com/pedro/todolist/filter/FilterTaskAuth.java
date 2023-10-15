package br.com.pedro.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.pedro.todolist.user.IUserRepository;
// import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// A tutora na ocasição decidiu continuar com outra forma de "middleware" por
// conta do ServeletRequest e ServletResponse, o que nos obrigaria a converter
// eles para HttpRequest e HttpResponse

// esta notation é importante
// @Component
// public class FilterTaskAuth implements Filter {

// @Override
// public void doFilter(ServletRequest request, ServletResponse response,
// FilterChain chain)
// throws IOException, ServletException {
// // isso aqui é como se fosse o next de um middleware no express
// chain.doFilter(request, response);
// }

// }

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // peger a autenticação (usuario e senha), pois estamos usando o Basic Auth,
        // TODO: estudar como seria com o Bearer token
        // validar usuario e senha

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks")) {

            var authorization = request.getHeader("Authorization");
            // no caso do basic auth ele retorna algo assim Basic cGVkcm9zb3ByYW5vOjEyMzQ1,
            // decodificado em base64

            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecode);
            // authString = pedrosoprano:12345
            String[] credentials = authString.split(":");

            String username = credentials[0];
            String password = credentials[1];

            var user = userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401, "Usuário sem autorização");
            } else {

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    // isso serve para passar algo para o meu controller, la no controller de /tasks
                    // posso passar HttpServletRequest request como segundo parâmetro de alhum
                    // método e então ser acesso ao atributo que foi acessado aqui atraves do método
                    // getAtribute
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }

        } else {
            filterChain.doFilter(request, response);
        }

    }

}