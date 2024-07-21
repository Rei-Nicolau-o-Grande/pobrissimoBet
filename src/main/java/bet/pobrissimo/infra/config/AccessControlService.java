package bet.pobrissimo.infra.config;

import bet.pobrissimo.core.enums.RoleEnum;
import bet.pobrissimo.infra.exception.AccessDeniedException;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.UUID;

public class AccessControlService {

    /**
     * Verifica se o usuário atual tem permissão para acessar um recurso identificado por userId.
     *
     * @see AuthenticatedCurrentUser para obter informações sobre o usuário atual.
     * @param userId O ID do usuário alvo.
     * @throws AccessDeniedException se o usuário atual não tem permissão.
     */

    public static void checkPermission(String userId) {
        UUID targetUserId = UUID.fromString(userId);
        UUID currentUserId = AuthenticatedCurrentUser.getUserId();
        Set<String> currentUserRoles = AuthenticatedCurrentUser.getUserRoles();

        if (!currentUserId.equals(targetUserId) &&
                !currentUserRoles.contains(RoleEnum.ADMIN.getName())) {
            throw new AccessDeniedException(
                    HttpStatus.FORBIDDEN,
                    HttpStatus.FORBIDDEN.value(),
                    HttpStatus.FORBIDDEN.getReasonPhrase(),
                    "Acesso Negado"
            );
        }
    }
}
