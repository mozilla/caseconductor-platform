USE tcm;

set foreign_key_checks =0;

ALTER TABLE
    Tag CHANGE tag name VARCHAR(45) NOT NULL COMMENT
    'Short text associated with the entity.';

ALTER TABLE
    TestCaseTag CHANGE testCaseId testCaseVersionId INT NOT NULL COMMENT
    'Associated test case version id.'

