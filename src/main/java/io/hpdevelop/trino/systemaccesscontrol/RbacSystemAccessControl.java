/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hpdevelop.trino.systemaccesscontrol;

import io.trino.spi.security.Identity;
import io.trino.spi.security.SystemAccessControl;
import io.trino.spi.security.SystemSecurityContext;
import io.trino.spi.security.TrinoPrincipal;

// import io.airlift.log.Logger;

import java.util.logging.Logger;
import java.util.Map;
import java.util.Optional;
import java.security.Principal;

import static io.trino.spi.security.AccessDeniedException.denyCreateRole;
import static io.trino.spi.security.AccessDeniedException.denySetUser;
import static io.trino.spi.security.AccessDeniedException.denyExecuteQuery;
import static io.trino.spi.security.AccessDeniedException.denyImpersonateUser;
import static java.util.logging.Level.INFO;

public class RbacSystemAccessControl
        implements SystemAccessControl
{
    private final String dbUrl;
    private static final Logger log = Logger.getLogger("rbac");

    RbacSystemAccessControl(Map<String, String> config)
    {
        this.dbUrl = config.get("rbac.db.url");
    }
    /**
     * Check if identity is allowed to create the specified role.
     *
     * @throws AccessDeniedException if not allowed
     */

    @Override
    public void checkCanCreateRole(SystemSecurityContext context, String role, Optional<TrinoPrincipal> grantor)
    {
        String userName = context.getIdentity().getUser();
        String logs = "checking if can create role "+role+" to user "+userName;
        log.log(INFO,logs);
        denyCreateRole(userName);
    }

    /**
     * Check if the identity is allowed impersonate the specified user.
     *
     * @throws AccessDeniedException if not allowed
     */
    public void checkCanImpersonateUser(SystemSecurityContext context, String userName)
    {
        String user = context.getIdentity().getUser();
        String logs = "checking if can impersonate role "+user+" to user "+userName;
        log.log(INFO,logs);
        //denyImpersonateUser(context.getIdentity().getUser(), userName);
    }

    /**
     * Check if the principal is allowed to be the specified user.
     *
     * @throws AccessDeniedException if not allowed
     * @deprecated use user mapping and {@link #checkCanImpersonateUser} instead
     */
    @Override
    @Deprecated
    public void checkCanSetUser(Optional<Principal> principal, String userName)
    {
        return;
        // denySetUser(principal, userName);
    }

    /**
     * Checks if identity can execute a query.
     *
     * @throws AccessDeniedException if not allowed
     */
    @Override
    public void checkCanExecuteQuery(SystemSecurityContext context)
    {
        String nameLog = "user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denyExecuteQuery();
        }
    }
}
