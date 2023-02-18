# Solution

## Prerequisite
1)Run docker-compose up

2)Create table inside postGis database
```
CREATE TABLE zombie_locations (
	zombie_id uuid NOT NULL,
	geom geometry(point, 4326) NOT NULL,
	updated_at timestamp NOT NULL
);

CREATE TABLE captured_zombie (
	zombie_id uuid NOT NULL,
	updated_at timestamp NOT NULL
);
```
## How to produce event
There is package com.zombie.solution.local where are defined one controller
and 2 producer that can be used to produce kafka event