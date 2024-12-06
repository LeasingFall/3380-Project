create table trafficViolations (
    "seqid" INTEGER,
    "date" INTEGER, -- Check DATE type option format: 'YYYY-MM-DD' and > < = works on this 
    "time" INTEGER,
    "description" TEXT,
    "vid" INTEGER,
    "pid" INTEGER,
    PRIMARY KEY ("seqid"),
    FOREIGN KEY ("pid") REFERENCES "driver"("pid"),
    FOREIGN KEY ("vid") REFERENCES "vehicle"("vid")
);

create table person (
    "pid" INTEGER,
    "injurySeverity" TEXT,
    "atFault" BOOLEAN;
    "substanceAbuse" TEXT,
    PRIMARY KEY ("pid"),
);

create table driver (
    "pid" INTEGER PRIMARY KEY,
    "distractedBy" TEXT,
    FOREIGN KEY ("pid") REFERENCES "person"("pid")
);

create table drives (
    "pid" INTEGER,
    "vid" INTEGER,
    FOREIGN KEY ("pid") REFERENCES "driver"("pid"),
    FOREIGN KEY ("vid") REFERENCES "vehicle"("vid")
);

create table nonMotorists (
    "pid" INTEGER PRIMARY KEY,
    "type" TEXT,
    "actions" TEXT,
    "movement" TEXT,
    FOREIGN KEY ("pid") REFERENCES "person"("pid")
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
    PRIMARY KEY ("longitude", "latitude")
);

create table has ( -- between environmental conditions and location
    "longitude" DECIMAL(10,8),
    "latitude" DECIMAL(10,8),
	"ecid" INTEGER,
	PRIMARY KEY("longitude","latitude", "ecid"),
	FOREIGN KEY("ecid") REFERENCES "environmentalConditions"("ecid"),
	FOREIGN KEY("longitude") REFERENCES "location"("longitude"),
	FOREIGN KEY("latitude") REFERENCES "location"("latitude")
);

create table environmentalConditions (
    "ecid" INTEGER AUTOINCREMENT,
    "weather" TEXT,
    "surfaceCondition" TEXT,
    "lightCondition" TEXT,
    PRIMARY KEY ("ecid")
);

create table collision (
    "cid" INTEGER AUTOINCREMENT,
    "crashDate" INTEGER,
    "crashTime" INTEGER,
    "longitude" DECIMAL(10,8),
    "latitude" DECIMAL(10,8),
    "pid" INTEGER,
    "vid" INTEGER,
    "reportNumber" INTEGER,
    "firstImpactLocation" TEXT,
    "damageExtent" TEXT,
    "collisionExtent" TEXT,
    PRIMARY KEY ("cid"),
    FOREIGN KEY("longitude") REFERENCES "location"("longitude"),
	FOREIGN KEY("latitude") REFERENCES "location"("latitude"),
	FOREIGN KEY("reportNumber") REFERENCES "report"("reportNumber"),
	FOREIGN KEY("pid") REFERENCES "driver"("pid")
);

create table relatedWith ( -- Between nonMotorist and collisions
    "pid" INTEGER,
    "cid" INTEGER,
    PRIMARY KEY ("cid", "pid"),
    FOREIGN KEY("cid") REFERENCES "collision"("cid"),
    FOREIGN KEY("pid") REFERENCES "nonMotorist"("pid")
);

create table report (
    "reportNumber" INTEGER,
    "reportType" TEXT,
    PRIMARY KEY ("reportNumber") 
);