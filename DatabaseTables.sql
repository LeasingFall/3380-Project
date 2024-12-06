drop table if exists relatedWith;
drop table if exists Drives;
drop table if exists has;
drop table if exists collision;
drop table if exists report;
drop table if exists environmentalConditions;
drop table if exists nonMotorists;
drop table if exists trafficViolations;
drop table if exists location;
drop table if exists vehicle;
drop table if exists driver;

create table driver (
    "pid" INTEGER,
    "injurySeverity" TEXT,
    "atFault" TEXT,
    "substanceAbuse" TEXT,
    "distractedBy" TEXT,
     PRIMARY KEY ("pid")
);

create table vehicle (
    "vid" INTEGER,
    "make" TEXT,
    "model" TEXT,
    "year" INTEGER,
    "bodyType" TEXT,
    "vehicleMovement" TEXT,
    PRIMARY KEY ("vid")
);

create table location (
    "longitude" DECIMAL(10,8), -- 10 digits and 8 after decimal point
    "latitude" DECIMAL(10,8), -- Used these for presicion
    "speedLimit" INTEGER,
    "trafficCtrl" TEXT,
    "roadName" TEXT,
    "routeType" TEXT,
    "vehicleGoingDir" TEXT,
    "crossStreet" TEXT,
    PRIMARY KEY ("latitude", "longitude")
);

create table trafficViolations (
    "seqid" INTEGER,
    "date" INTEGER, -- Check DATE type option format: 'YYYY-MM-DD' and > < = works on this 
    "time" INTEGER,
    "description" TEXT,
    "outcome" TEXT,
    "vid" INTEGER,
    "pid" INTEGER,
    "longitude" DECIMAL(10,8),
    "latitude" DECIMAL(10,8),
    PRIMARY KEY ("seqid"),
    FOREIGN KEY ("pid") REFERENCES "driver"("pid"),
    FOREIGN KEY ("vid") REFERENCES "vehicle"("vid"),
    FOREIGN KEY ("latitude", "longitude") REFERENCES "location"("latitude", "longitude")
);

create table nonMotorists (
    "pid" INTEGER PRIMARY KEY,
    "type" TEXT,
    "actions" TEXT,
    "movement" TEXT,
    "injurySeverity" TEXT,
    "atFault" TEXT,
    "substanceAbuse" TEXT
);

create table environmentalConditions (
    "ecid" INTEGER PRIMARY KEY IDENTITY(1,1),
    "weather" TEXT,
    "surfaceCondition" TEXT,
    "lightCondition" TEXT
);

create table report (
    "reportNumber" INTEGER,
    "reportType" TEXT,
    PRIMARY KEY ("reportNumber") 
);

create table collision (
    "cid" INTEGER PRIMARY KEY IDENTITY(1,1),
    "crashDate" INTEGER,
    "crashTime" INTEGER,
    "longitude" DECIMAL(10,8),
    "latitude" DECIMAL(10,8),
    "pid" INTEGER,
    "vid" INTEGER,
    "reportNumber" INTEGER,
    "firstImpactLocation" TEXT,
    "damageExtent" TEXT,
    "collisionType" TEXT,
    FOREIGN KEY ("latitude", "longitude") REFERENCES "location"("latitude", "longitude"),
	FOREIGN KEY("reportNumber") REFERENCES "report"("reportNumber"),
	FOREIGN KEY("pid") REFERENCES "driver"("pid")
);

create table has ( -- between environmental conditions and location
    "longitude" DECIMAL(10,8),
    "latitude" DECIMAL(10,8),
	"ecid" INTEGER,
	PRIMARY KEY("longitude","latitude", "ecid"),
	FOREIGN KEY("ecid") REFERENCES "environmentalConditions"("ecid"),
	FOREIGN KEY ("latitude", "longitude") REFERENCES "location"("latitude", "longitude")
);

create table drives (
    "pid" INTEGER,
    "vid" INTEGER,
    FOREIGN KEY ("pid") REFERENCES "driver"("pid"),
    FOREIGN KEY ("vid") REFERENCES "vehicle"("vid")
);

create table relatedWith ( -- Between nonMotorist and collisions
    "pid" INTEGER,
    "cid" INTEGER,
    PRIMARY KEY ("cid", "pid"),
    FOREIGN KEY("cid") REFERENCES "collision"("cid"),
    FOREIGN KEY("pid") REFERENCES "nonMotorists"("pid")
);
