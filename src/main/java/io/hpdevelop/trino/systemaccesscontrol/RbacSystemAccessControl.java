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

import io.trino.spi.connector.CatalogSchemaName;
import io.trino.spi.connector.CatalogSchemaRoutineName;
import io.trino.spi.connector.CatalogSchemaTableName;
import io.trino.spi.connector.SchemaTableName;
import io.trino.spi.eventlistener.EventListener;
import io.trino.spi.function.FunctionKind;
import io.trino.spi.type.Type;
import io.trino.spi.security.Identity;
import io.trino.spi.security.SystemAccessControl;
import io.trino.spi.security.SystemSecurityContext;
import io.trino.spi.security.TrinoPrincipal;

// import io.airlift.log.Logger;

import java.util.logging.Logger;
import java.util.Map;
import java.util.Optional;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.trino.spi.security.AccessDeniedException.denyCreateRole;
import static io.trino.spi.security.AccessDeniedException.denySetUser;
import static io.trino.spi.security.AccessDeniedException.denyExecuteQuery;
import static io.trino.spi.security.AccessDeniedException.denyImpersonateUser;
import static io.trino.spi.security.AccessDeniedException.denyCatalogAccess;
import static io.trino.spi.security.AccessDeniedException.denyShowSchemas;
import static io.trino.spi.security.AccessDeniedException.denyShowTables;
import static io.trino.spi.security.AccessDeniedException.denyShowColumns;
import static io.trino.spi.security.AccessDeniedException.denySelectColumns;
import static java.util.logging.Level.INFO;
import static java.lang.String.format;
import static java.util.Collections.emptySet;

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
        String logs = "(checkCanCreateRole) checking if can create role "+role+" to user "+userName;
        log.log(INFO,logs);
        return;
        // denyCreateRole(userName);
    }

    /**
     * Check if the identity is allowed impersonate the specified user.
     *
     * @throws AccessDeniedException if not allowed
     */
    public void checkCanImpersonateUser(SystemSecurityContext context, String userName)
    {
        String user = context.getIdentity().getUser();
        String logs = "(checkCanImpersonateUser) checking if can impersonate role "+user+" to user "+userName;
        log.log(INFO,logs);
        return;
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
        String nameLog = "(checkCanExecuteQuery) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denyExecuteQuery();
        }
        return;
    }

    /**
     * Check if identity is allowed to access the specified catalog
     *
     * @throws AccessDeniedException if not allowed
     */
    public void checkCanAccessCatalog(SystemSecurityContext context, String catalogName)
    {
        String nameLog = "(checkCanAccessCatalog) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denyCatalogAccess(catalogName);
        }
        return;
        
    }

    /**
     * Filter the list of catalogs to those visible to the identity.
     */
    public Set<String> filterCatalogs(SystemSecurityContext context, Set<String> catalogs)
    {
        String nameLog = "(filterCatalogs) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            return emptySet();
        }
        return catalogs;
    }

    /**
     * Check if identity is allowed to execute SHOW SCHEMAS in a catalog.
     * <p>
     * NOTE: This method is only present to give users an error message when listing is not allowed.
     * The {@link #filterSchemas} method must filter all results for unauthorized users,
     * since there are multiple ways to list schemas.
     *
     * @throws AccessDeniedException if not allowed
     */
    public void checkCanShowSchemas(SystemSecurityContext context, String catalogName)
    {
        String nameLog = "(checkCanShowSchemas) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denyShowSchemas();
        }
        return;
        
    }

    /**
     * Filter the list of schemas in a catalog to those visible to the identity.
     */
    public Set<String> filterSchemas(SystemSecurityContext context, String catalogName, Set<String> schemaNames)
    {
        String nameLog = "(filterSchemas) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            return emptySet();
        }
        return schemaNames;
    }

    /**
     * Check if identity is allowed to show metadata of tables by executing SHOW TABLES, SHOW GRANTS etc. in a catalog.
     * <p>
     * NOTE: This method is only present to give users an error message when listing is not allowed.
     * The {@link #filterTables} method must filter all results for unauthorized users,
     * since there are multiple ways to list tables.
     *
     * @throws AccessDeniedException if not allowed
     */
    public void checkCanShowTables(SystemSecurityContext context, CatalogSchemaName schema)
    {
        String nameLog = "(checkCanShowTables) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denyShowTables(schema.toString());
        }
        return;
    }

    /**
     * Filter the list of tables and views to those visible to the identity.
     */
    public Set<SchemaTableName> filterTables(SystemSecurityContext context, String catalogName, Set<SchemaTableName> tableNames)
    {
        String nameLog = "(filterTables) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            return emptySet();
        }
        return tableNames;
    }

    /**
     * Check if identity is allowed to show columns of tables by executing SHOW COLUMNS, DESCRIBE etc.
     * <p>
     * NOTE: This method is only present to give users an error message when listing is not allowed.
     * The {@link #filterColumns} method must filter all results for unauthorized users,
     * since there are multiple ways to list columns.
     *
     * @throws io.trino.spi.security.AccessDeniedException if not allowed
     */
    public void checkCanShowColumns(SystemSecurityContext context, CatalogSchemaTableName table)
    {
        String nameLog = "(checkCanShowColumns) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denyShowColumns(table.toString());
        }
        return;
    }

    /**
     * Filter the list of columns to those visible to the identity.
     */
    public Set<String> filterColumns(SystemSecurityContext context, CatalogSchemaTableName table, Set<String> columns)
    {
        String nameLog = "(filterColumns) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            return emptySet();
        }
        return columns;
    }

    /**
     * Check if identity is allowed to select from the specified columns in a relation.  The column set can be empty.
     *
     * @throws AccessDeniedException if not allowed
     */
    public void checkCanSelectFromColumns(SystemSecurityContext context, CatalogSchemaTableName table, Set<String> columns)
    {
        String nameLog = "(checkCanSelectFromColumns) user is "+context.getIdentity().getUser();
        log.log(INFO,nameLog);
        if(context.getIdentity().getUser().compareTo("test") != 0)
        {
            denySelectColumns(table.toString(), columns);
        }
        return;
    }
}
