/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2021 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package fish.payara.extensions.autoscale.groups;

import com.sun.enterprise.config.serverbeans.Config;
import com.sun.enterprise.config.serverbeans.RefContainer;
import com.sun.enterprise.config.serverbeans.customvalidators.NotDuplicateTargetName;
import com.sun.enterprise.config.serverbeans.customvalidators.NotTargetKeyword;
import com.sun.enterprise.config.serverbeans.customvalidators.ReferenceConstraint;
import fish.payara.enterprise.config.serverbeans.DeploymentGroup;
import org.glassfish.api.admin.config.Named;
import org.jvnet.hk2.config.Attribute;
import org.jvnet.hk2.config.Configured;

import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static org.glassfish.config.support.Constants.NAME_SERVER_REGEX;

/**
 * Parent interface for AutoScale Group implementation ConfigBeans.
 *
 * @author Andrew Pielage
 */
@Configured
@NotDuplicateTargetName(message="{sg.duplicate.name}", payload = ScalingGroup.class)
@ReferenceConstraint(skipDuringCreation = true, payload = ScalingGroup.class)
public interface ScalingGroup extends Named, Payload {

    @NotTargetKeyword(message="{sg.reserved.name}", payload = ScalingGroup.class)
    @Pattern(regexp = NAME_SERVER_REGEX, message = "{sg.invalid.name}", payload = ScalingGroup.class)
    @Override
    public String getName();

    /**
     * Points to a named {@link Config}. All server instances in the scaling group will share this config.
     *
     * @return The name of the {@link Config}
     */
    @Attribute(defaultValue = "default-config")
    @NotNull
    @Pattern(regexp = NAME_SERVER_REGEX)
    @NotTargetKeyword(message = "{config.reserved.name}", payload = Config.class)
    @ReferenceConstraint.RemoteKey(message = "{resourceref.invalid.configref}", type = Config.class)
    String getConfigRef();
    void setConfigRef();

    /**
     *
     * @return
     */
    @Attribute
    @NotNull
    @Pattern(regexp=NAME_SERVER_REGEX)
    @NotTargetKeyword(message="{dg.reserved.name}", payload = DeploymentGroup.class)
    @ReferenceConstraint.RemoteKey(message="{resourceref.invalid.dgref}", type = DeploymentGroup.class)
    @NotDuplicateDeploymentGroupTarget
    String getDeploymentGroupRef();
    void setDeploymentGroupRef();
}
