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

import io.trino.spi.security.SystemAccessControl;
import io.trino.spi.security.SystemSecurityContext;
import io.trino.spi.security.TrinoPrincipal;

import java.util.Optional;

import static io.trino.spi.security.AccessDeniedException.denyCreateRole;

public class RbacSystemAccessControl
        implements SystemAccessControl
{
    private final String dbUrl;

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
        String result = role + "plugin test~~";
        denyCreateRole(role);
    }
}
