--
-- Structure for table myfiles_myfileblob
--

DROP TABLE IF EXISTS myfiles_myfileblob;
CREATE TABLE myfiles_myfileblob (
id_myfile_blob int NOT NULL,
bucket_name_id varchar(100) default '' NOT NULL,
file_content_type varchar(255) default '' NOT NULL,
file_size int default '0' NOT NULL,
file_name varchar(255) default '' NOT NULL,
blob_value long varbinary,
PRIMARY KEY (id_myfile_blob)
);