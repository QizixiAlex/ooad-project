/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2017/5/31 23:29:05                           */
/*==============================================================*/

drop table if exists risk_check_item;

drop table if exists company_in_plan;

drop table if exists item_in_template;

drop table if exists risk_check;

drop table if exists company;

drop table if exists risk_check_plan;

drop table if exists risk_check_template;

drop table if exists risk_check_template_item;

/*==============================================================*/
/* Table: company                                               */
/*==============================================================*/
create table company
(
   id                   char(50) not null,
   name                 text not null,
   status               text,
   code                 text,
   industry_type        text,
   industry             text,
   trade                text,
   contact_name         text,
   contact_tel          text,
   primary key (id)
);

/*==============================================================*/
/* Table: company_in_plan                                       */
/*==============================================================*/
create table company_in_plan
(
   id_plan              int not null,
   id_company           char(50) not null,
   primary key (id_plan, id_company)
);

/*==============================================================*/
/* Table: item_in_template                                      */
/*==============================================================*/
create table item_in_template
(
   id_template_item     int not null,
   id_template          int not null,
   primary key (id_template_item, id_template)
);

/*==============================================================*/
/* Table: risk_check                                            */
/*==============================================================*/
create table risk_check
(
   id                   int not null auto_increment,
   id_plan              int,
   id_company           char(50),
   actual_finish_date   timestamp DEFAULT CURRENT_TIMESTAMP,
   status               text,
   primary key (id)
);

/*==============================================================*/
/* Table: risk_check_item                                       */
/*==============================================================*/
create table risk_check_item
(
   id                   int not null auto_increment,
   id_template_item     int not null,
   id_risk_check        int not null,
   status               text,
   finish_date          timestamp,
   primary key (id)
);

/*==============================================================*/
/* Table: risk_check_plan                                       */
/*==============================================================*/
create table risk_check_plan
(
   id                   int not null auto_increment,
   id_template          int not null,
   name                 text not null,
   start_date           timestamp,
   finish_date          timestamp,
   primary key (id)
);

/*==============================================================*/
/* Table: risk_check_template                                   */
/*==============================================================*/
create table risk_check_template
(
   id                   int not null auto_increment,
   name                 text not null,
   description          text,
   primary key (id)
);

/*==============================================================*/
/* Table: risk_check_template_item                              */
/*==============================================================*/
create table risk_check_template_item
(
   id                   int not null auto_increment,
   name                 text not null,
   content              text,
   primary key (id)
);

alter table company_in_plan add constraint FK_Reference_10 foreign key (id_company)
      references company (id) on delete restrict on update restrict;

alter table company_in_plan add constraint FK_Reference_9 foreign key (id_plan)
      references risk_check_plan (id) on delete restrict on update restrict;

alter table item_in_template add constraint FK_item_in_template foreign key (id_template_item)
      references risk_check_template_item (id) on delete restrict on update restrict;

alter table item_in_template add constraint FK_item_in_template2 foreign key (id_template)
      references risk_check_template (id) on delete restrict on update restrict;

alter table risk_check add constraint FK_Reference_11 foreign key (id_company)
      references company (id) on delete restrict on update restrict;

alter table risk_check add constraint FK_Reference_8 foreign key (id_plan)
      references risk_check_plan (id) on delete restrict on update restrict;

alter table risk_check_item add constraint FK_risk_check_for_item foreign key (id_risk_check)
      references risk_check (id) on delete restrict on update restrict;

alter table risk_check_item add constraint FK_template_item_for_item foreign key (id_template_item)
      references risk_check_template_item (id) on delete restrict on update restrict;

alter table risk_check_plan add constraint FK_template_for_plan foreign key (id_template)
      references risk_check_template (id) on delete restrict on update restrict;

