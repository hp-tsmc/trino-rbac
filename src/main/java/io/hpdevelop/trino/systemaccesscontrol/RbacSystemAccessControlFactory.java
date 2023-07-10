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
import io.trino.spi.security.SystemAccessControlFactory;

import java.util.Map;

public class RbacSystemAccessControlFactory
        implements SystemAccessControlFactory
{
    @Override
    public String getName()
    {
        return "rbac-hpdevelop";
    }

    @Override
    public SystemAccessControl create(Map<String, String> config)
    {
        if (config.isEmpty()) {
            throw new IllegalArgumentException("Role Based Access Control needs configurations for DB info!");
        }
        return new RbacSystemAccessControl(config);
    }
}
