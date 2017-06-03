INSERT INTO risk_check_template_item (id,name,content) VALUES (1,"checkfirealarm","checkifbroken");
INSERT INTO risk_check_template(id,name,description) VALUES (1,"firsttemplate","firstone");
INSERT INTO company(id,name) VALUES ("a123","microsoft");
INSERT INTO risk_check_plan(id,id_template,name,start_date,finish_date) VALUES (1,1,"firstplan",'2013-08-05 18:19:03','2013-08-05 18:19:03');
INSERT INTO risk_check(id,id_plan,id_company) VALUES (1,1,"a123")

