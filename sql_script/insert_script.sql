-- ===== INSERT SAMPLE DATA =====

-- ===== INTERESTS =====
INSERT INTO interests (name) VALUES 
('Java'), 
('Spring Boot'), 
('ReactJS'), 
('Data Science'), 
('UI/UX Design'),
('Python'),
('Vue.js'),
('AWS');

-- ===== USERS =====
INSERT INTO users (email, full_name, password, user_type, preferred_language, job_title) VALUES 
('john.doe@email.com', 'John Doe', 'hashed_password_123', 'User', 'English', 'Student'),
('jane.smith@email.com', 'Jane Smith', 'hashed_password_456', 'User', 'Vietnamese', 'Software Engineer'),
('admin@email.com', 'Admin User', 'hashed_password_admin', 'Admin', 'Vietnamese', 'Administrator'),
('bob.wilson@email.com', 'Bob Wilson', 'hashed_password_789', 'User', 'English', 'Data Analyst'),
('alice.johnson@email.com', 'Alice Johnson', 'hashed_password_000', 'User', 'English', 'UX Designer');

-- ===== USER INTERESTS =====
INSERT INTO user_interests (user_id, interest_id) VALUES 
(1, 1), -- John: Java
(1, 2), -- John: Spring Boot
(2, 3), -- Jane: ReactJS
(2, 6), -- Jane: Python
(4, 4), -- Bob: Data Science
(4, 6), -- Bob: Python
(5, 5), -- Alice: UI/UX Design
(5, 7); -- Alice: Vue.js

-- ===== COURSES =====
INSERT INTO courses (name, description, image, author, type, price, modules_count) VALUES 
('Java Fundamentals', 'Learn Java basics from scratch', 'java-basics.jpg', 'John Developer', 'Self-Paced', 49.99, 10),
('Spring Boot Mastery', 'Complete guide to Spring Boot framework', 'spring-boot.jpg', 'Spring Expert', 'Self-Paced', 79.99, 10),
('ReactJS Advanced', 'Advanced React concepts and patterns', 'react-advanced.jpg', 'React Guru', 'Live Boot Camp', 199.99, 8),
('Python for Data Science', 'Data science with Python and pandas', 'python-ds.jpg', 'Data Scientist', 'Self-Paced', 59.99, 10),
('Web Design Bootcamp', 'UI/UX design principles and tools', 'web-design.jpg', 'Design Master', 'Live Boot Camp', 249.99, 8),
('Java Advanced Concepts', 'Deep dive into Java concurrency', 'java-advanced.jpg', 'Java Expert', 'Live Boot Camp', 179.99, 8);

-- ===== COURSE COHORTS (for Live Boot Camp) =====
INSERT INTO course_cohorts (course_id, cohort_name, start_date, end_date, max_students, enrolled_count, status) VALUES 
(3, 'K15 - Feb 2026', '2026-02-15', '2026-04-05', 30, 2, 'UPCOMING'),
(3, 'K16 - Apr 2026', '2026-04-20', '2026-06-10', 30, 0, 'UPCOMING'),
(5, 'UX-K5 - Feb 2026', '2026-02-20', '2026-04-10', 25, 1, 'UPCOMING'),
(5, 'UX-K6 - Apr 2026', '2026-04-15', '2026-06-05', 25, 0, 'UPCOMING'),
(6, 'Java-Advanced-K3 - Mar 2026', '2026-03-01', '2026-04-21', 20, 0, 'UPCOMING');

-- ===== MODULES =====
-- Java Fundamentals (Self-Paced - 10 modules)
INSERT INTO modules (course_id, module_number, title, description) VALUES 
(1, 1, 'Introduction to Java', 'Getting started with Java'),
(1, 2, 'Variables and Data Types', 'Understanding java variables'),
(1, 3, 'Control Flow', 'If statements and loops'),
(1, 4, 'Functions/Methods', 'Creating and calling methods'),
(1, 5, 'OOP Basics', 'Classes and objects'),
(1, 6, 'Inheritance', 'Extending classes'),
(1, 7, 'Polymorphism', 'Method overriding and interfaces'),
(1, 8, 'Collections', 'Array, List, Map, Set'),
(1, 9, 'Exception Handling', 'Try, catch, finally'),
(1, 10, 'File I/O', 'Reading and writing files');

-- Spring Boot Mastery (Self-Paced - 10 modules)
INSERT INTO modules (course_id, module_number, title, description) VALUES 
(2, 1, 'Spring Boot Setup', 'Creating and configuring Spring Boot projects'),
(2, 2, 'Annotations', 'Understanding annotations in Spring'),
(2, 3, 'REST APIs', 'Building REST endpoints'),
(2, 4, 'Database Integration', 'JPA and database setup'),
(2, 5, 'Validation', 'Input validation'),
(2, 6, 'Security Basics', 'Authentication and authorization'),
(2, 7, 'Testing', 'Unit and integration testing'),
(2, 8, 'Logging', 'Configuring logging'),
(2, 9, 'Deployment', 'Deploying Spring Boot apps'),
(2, 10, 'Best Practices', 'Code organization and architecture');

-- ReactJS Advanced (Live Boot Camp - 8 modules)
INSERT INTO modules (course_id, module_number, title, description) VALUES 
(3, 1, 'React Hooks Deep Dive', 'Advanced hooks patterns'),
(3, 2, 'State Management', 'Redux and Context API'),
(3, 3, 'Performance Optimization', 'Code splitting and lazy loading'),
(3, 4, 'Testing React', 'Jest and React Testing Library'),
(3, 5, 'Build Tools', 'Webpack and Vite'),
(3, 6, 'API Integration', 'Fetching and handling async data'),
(3, 7, 'Project Structure', 'Building scalable React applications'),
(3, 8, 'Capstone Project', 'Building a complete React application');

-- ===== LESSONS =====
-- Java Fundamentals - Module 1
INSERT INTO lessons (module_id, lesson_number, title, description, video_url, duration_minutes, is_preview) VALUES 
(1, 1, 'What is Java?', 'Introduction to Java programming', 'https://video.example.com/java-1-1.mp4', 15, 1),
(1, 2, 'Setting up JDK', 'Installing Java Development Kit', 'https://video.example.com/java-1-2.mp4', 20, 0),
(1, 3, 'First Java Program', 'Writing your first Hello World program', 'https://video.example.com/java-1-3.mp4', 25, 0),
(1, 4, 'IDE Setup', 'Installing and configuring IntelliJ IDEA', 'https://video.example.com/java-1-4.mp4', 18, 0);

-- Java Fundamentals - Module 2
INSERT INTO lessons (module_id, lesson_number, title, description, video_url, duration_minutes, is_preview) VALUES 
(2, 1, 'Variables Basics', 'Understanding variables in Java', 'https://video.example.com/java-2-1.mp4', 22, 1),
(2, 2, 'Primitive Data Types', 'Exploring all primitive types', 'https://video.example.com/java-2-2.mp4', 30, 0),
(2, 3, 'String Operations', 'Working with Strings', 'https://video.example.com/java-2-3.mp4', 25, 0);

-- Spring Boot - Module 1
INSERT INTO lessons (module_id, lesson_number, title, description, video_url, duration_minutes, is_preview) VALUES 
(11, 1, 'Spring Boot Introduction', 'Why use Spring Boot?', 'https://video.example.com/springboot-1-1.mp4', 20, 1),
(11, 2, 'Project Setup', 'Creating a Spring Boot project', 'https://video.example.com/springboot-1-2.mp4', 25, 0),
(11, 3, 'Properties Configuration', 'Configuring application.properties', 'https://video.example.com/springboot-1-3.mp4', 18, 0);

-- React - Module 1
INSERT INTO lessons (module_id, lesson_number, title, description, video_url, duration_minutes, is_preview) VALUES 
(19, 1, 'Hooks State and Effect', 'Deep dive into useState and useEffect', 'https://video.example.com/react-1-1.mp4', 30, 1),
(19, 2, 'Custom Hooks', 'Creating custom hooks', 'https://video.example.com/react-1-2.mp4', 28, 0),
(19, 3, 'Hooks Performance', 'Optimizing with hooks', 'https://video.example.com/react-1-3.mp4', 35, 0);

-- ===== SHOPPING CARTS =====
INSERT INTO carts (user_id, course_id, selected_cohort_id, price) VALUES 
(1, 1, NULL, 49.99), -- John: Java Fundamentals
(2, 3, 1, 199.99), -- Jane: ReactJS Advanced (Cohort K15)
(4, 4, NULL, 59.99); -- Bob: Python for Data Science

-- ===== ORDERS =====
INSERT INTO orders (user_id, total_price, discount_price, final_price, status) VALUES 
(1, 129.98, 10.00, 119.98, 'COMPLETED'), -- John: Java + Spring Boot
(2, 199.99, 0, 199.99, 'COMPLETED'), -- Jane: ReactJS
(4, 99.98, 5.00, 94.98, 'COMPLETED'), -- Bob: Python
(5, 249.99, 0, 249.99, 'PENDING'); -- Alice: UX Design (unpaid)

-- ===== ORDER ITEMS =====
INSERT INTO order_items (order_id, course_id, cohort_id, price_at_purchase) VALUES 
(1, 1, NULL, 49.99), -- John: Java
(1, 2, NULL, 79.99), -- John: Spring Boot
(2, 3, 1, 199.99), -- Jane: ReactJS Cohort K15
(3, 4, NULL, 59.99), -- Bob: Python
(4, 5, 3, 249.99); -- Alice: UX Design Cohort

-- ===== PAYMENTS =====
INSERT INTO payments (order_id, user_id, amount, payment_method, transaction_id, status) VALUES 
(1, 1, 119.98, 'VNPAY', 'TXN-001-VNPAY', 'SUCCESS'),
(2, 2, 199.99, 'PAYPAL', 'TXN-002-PAYPAL', 'SUCCESS'),
(3, 4, 94.98, 'CREDIT_CARD', 'TXN-003-CC', 'SUCCESS'),
(4, 5, 249.99, 'VNPAY', 'TXN-004-VNPAY', 'PENDING');

-- ===== ENROLLMENTS =====
INSERT INTO enrollments (user_id, course_id, order_id, cohort_id, type, status, progress_percentage, completed_lessons) VALUES 
(1, 1, 1, NULL, 'SELF_PACED','ACTIVE', 25, 2), -- John: Java (Self-Paced)
(1, 2, 1, NULL, 'LIVE','ACTIVE', 10, 1), -- John: Spring Boot
(2, 3, 2, 1, 'LIVE','ACTIVE', 50, 4), -- Jane: ReactJS (Live Cohort)
(4, 4, 3, NULL, 'SELF_PACED','ACTIVE', 30, 3), -- Bob: Python
(5, 5, 4, 3, 'SELF_PACED','PENDING', 0, 0); -- Alice: UX Design (not started yet)

-- ===== USER LESSON PROGRESS =====
INSERT INTO user_lesson_progress (user_id, lesson_id, is_completed, completed_at) VALUES 
(1, 1, 1, '2026-02-10 10:30:00'),   -- John completed Java lesson 1
(1, 2, 1, '2026-02-11 14:20:00'),   -- John completed Java lesson 2
(1, 8, 1, '2026-02-12 09:00:00'),   -- John completed Spring Boot lesson 1 (lesson_id=8, NOT 14!)
(2, 11, 1, '2026-02-09 16:45:00'),  -- Jane completed React lesson 1 (lesson_id=11, NOT 19!)
(2, 12, 1, '2026-02-10 11:15:00'),  -- Jane completed React lesson 2 (lesson_id=12, NOT 20!)
(2, 13, 1, '2026-02-11 13:30:00'),  -- Jane completed React lesson 3 (lesson_id=13, NOT 21!)
(4, 5, 1, '2026-02-08 15:20:00'),   -- Bob completed Python lesson 1 (Java M2 L1)
(4, 6, 1, '2026-02-09 11:45:00'),   -- Bob completed Python lesson 2 (Java M2 L2)
(4, 7, 1, '2026-02-10 14:30:00');   -- Bob completed Python lesson 3 (Java M2 L3)