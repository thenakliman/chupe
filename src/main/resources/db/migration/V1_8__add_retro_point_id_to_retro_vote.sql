ALTER TABLE retrospection_vote DROP COLUMN retrospection;
ALTER TABLE retrospection_vote DROP COLUMN up_vote;
ALTER TABLE retrospection_vote ADD retro_point_id INTEGER REFERENCES retrospection_point(id);