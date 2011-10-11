USE tcm;

DROP TABLE IF EXISTS AttachmentTestCaseVersion;
DROP TABLE IF EXISTS UserPreference;

ALTER TABLE AccessRole ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Attachment ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Country ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE CountryLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Company ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EntityExternalBug ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Environment ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentGroup ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentGroupEnvironment ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentProfile ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentProfileEnvironment ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentProfileEnvironmentGroup ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Locale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Permission ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE ParentDependableEnvironment ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Product ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE ProductComponent ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE ProductVersion ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE RolePermission ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Tag ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE Team ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TeamUser ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TeamUserRole ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCase ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCaseProductComponent ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCaseStep ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCaseTag ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCaseVersion ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCycle ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestPlan ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestPlanTestSuite ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRun ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunResult ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunTestCase ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunTestCaseAssignment ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestSuite ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestSuiteTestCase ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE User ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE UserRole ADD (deleted TINYINT(1) DEFAULT '0');

ALTER TABLE ApprovalStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE ApprovalStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE AttachmentType ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE AttachmentTypeLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EntityType ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EntityTypeLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCaseStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCaseStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCycleStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestCycleStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestPlanStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestPlanStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunResultStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunResultStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestRunStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestSuiteStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE TestSuiteStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE UserStatus ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE UserStatusLocale ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentType ADD (deleted TINYINT(1) DEFAULT '0');
ALTER TABLE EnvironmentTypeLocale ADD (deleted TINYINT(1) DEFAULT '0');

-- new permissions to handle deletes
insert into Permission (permissionId, permissionCode, name, assignable, deleted) values (45, 'PERMISSION_DELETED_ENTITY_VIEW', '', '1', '0');
insert into Permission (permissionId, permissionCode, name, assignable, deleted) values (46, 'PERMISSION_DELETED_ENTITY_UNDO', '', '1', '0');