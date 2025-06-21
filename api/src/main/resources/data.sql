-- Add dummy users (passwords are encrypted versions of 'password')
INSERT OR IGNORE INTO users (id, username, password, enabled, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'admin@example.com',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT OR IGNORE INTO users (id, username, password, enabled, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'user1@example.com',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT OR IGNORE INTO users (id, username, password, enabled, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440002',
    'user2@example.com',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',
    1,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT OR IGNORE INTO users (id, username, password, enabled, created_at, updated_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440003',
    'user3@example.com',
    '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW',
    0,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);
