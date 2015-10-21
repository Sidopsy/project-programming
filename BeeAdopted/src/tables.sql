/**
*	This is the master document of table declarations for project group 11's application.
*
*	DO NOT ALTER UNLESS YOU HAVE PERMISSION!
*
*	@MT 16/10 2015
*/

CREATE TABLE Agencies (
	ID			INTEGER			PRIMARY KEY NOT NULL,
	OrgNo		CHAR(10)		UNIQUE NOT NULL,
	Name		VARCHAR(255)	UNIQUE,
	Email		VARCHAR(255)	UNIQUE,
	Phone		CHAR(10)		UNIQUE,
	Password	VARCHAR(255) 	DEFAULT ('abc123') NOT NULL,
	Salt		CHAR(64)		UNIQUE,
	Logo		VARCHAR(255)	UNIQUE
	);

CREATE TABLE Addresses (
	ID			INTEGER			PRIMARY KEY NOT NULL,
	Street		VARCHAR(255),
	ZIP			INTEGER,
	City		VARCHAR(255),
	AgencyID	INTEGER			REFERENCES Agencies (ID) ON DELETE CASCADE
	);
	
CREATE TABLE Listings (
	ID			INTEGER			PRIMARY KEY NOT NULL,
	Pictures	VARCHAR(255),
	Name		VARCHAR(255),
	Gender		CHAR(6),
	Species		VARCHAR(255),
	Type		VARCHAR(255),
	Age			INTEGER,
	Description	text,
	StartDate	DATE,
	EndDate		DATE,
	AgencyID	INTEGER			REFERENCES Agencies (ID) ON DELETE CASCADE
	);
		
CREATE TABLE Ratings (
	ID			INTEGER			PRIMARY KEY NOT NULL,
	Rating		INTEGER			DEFAULT (0),
	Comment		text,
	AgencyID	INTEGER			REFERENCES Agencies	(ID) ON DELETE CASCADE
	);
	

	