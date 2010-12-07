package com.utest.persistence;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = { "/context-application-test.xml" })
@TransactionConfiguration(transactionManager = "transactionManagerTest", defaultRollback = false)
@Transactional
@TestExecutionListeners( { DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public abstract class AbstractDatabaseTestCase extends AbstractTestNGSpringContextTests
{
}
