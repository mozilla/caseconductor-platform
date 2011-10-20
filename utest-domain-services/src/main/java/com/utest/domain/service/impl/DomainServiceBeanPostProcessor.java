/**
 *
 * Licensed under the GNU General Public License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.gnu.org/licenses/gpl.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 * @author Miguel Bautista
 *
 * copyright 2010 by uTest 
 */
package com.utest.domain.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import com.utest.domain.service.Initializable;

/**
 * A <code>BeanPostProcessor</code> that will process special configurations for
 * our domain service beans. For example, loading static data after the bean is
 * instantiated.
 */
public class DomainServiceBeanPostProcessor implements BeanPostProcessor, Ordered
{

	final Logger	logger	= LogManager.getLogger(DomainServiceBeanPostProcessor.class);

	/**
	 * Implementation of <code>Ordered</code> interface.
	 */
	@Override
	public int getOrder()
	{
		return Ordered.LOWEST_PRECEDENCE;
	}

	/**
	 * Process before bean initialization.
	 */
	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException
	{
		logger.debug("DomainServiceBeanPostProcessor postProcessBeforeInitialization: " + beanName);
		return bean;
	}

	/**
	 * Process after bean initialization.
	 */
	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException
	{
		logger.debug("DomainServiceBeanPostProcessor postProcessAfterInitialization: " + beanName);
		if (bean instanceof Initializable)
		{
			((Initializable) bean).initialize();
		}

		return bean;
	}
}
