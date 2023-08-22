create table cart (
    id  serial not null,
    sum float8,
    primary key (id)
);

create table category (
    id  serial not null,
    description varchar(255),
    title varchar(64),
    primary key (id)
);

create table good (
    id  serial not null,
    active boolean default true,
    description varchar(2048) not null,
    price float8,
    quantity int4 check (quantity>=0),
    small_image_link varchar(255),
    title varchar(64) not null,
    category_id int4 not null,
    primary key (id)
);

create table news (
    id  serial not null,
    date date,
    description varchar(255),
    news_image_link varchar(255),
    title varchar(32),
    primary key (id)
);

create table orders (
    id  serial not null,
    order_date timestamp,
    status int4,
    cart_id int4,
    person_id int4,
    primary key (id)
);

create table person (
    id  serial not null,
    activation_code varchar(255),
    active boolean default true,
    address varchar(255),
    age int4 check (age>=0) default 0,
    created_at timestamp,
    email varchar(255) not null unique,
    firstname varchar(30),
    lastname varchar(30),
    username varchar(30) not null unique,
    password varchar(255) not null,
    reputation int4,
    cart_id int4,
    role_id int4,
    primary key (id)
);

create table product_types (
    id  serial not null,
    type_name varchar(255),
    primary key (id)
);

create table products (
    id  serial not null,
    added_at timestamp,
    count int4,
    name varchar(100),
    price int4 check (price>=1),
    cart_id int4,
    good_id int4,
    type_id int4,
    primary key (id)
);

create table role (
    id  serial not null,
    name varchar(32) not null unique,
    primary key (id)
);

create table sales (
    order_id int4 not null,
    product_id int4 not null
);

alter table category
    add constraint category_title_unique
    unique (title);

alter table good
    add constraint good_category_fk
    foreign key (category_id) references category;

alter table orders
    add constraint orders_cart_fk
    foreign key (cart_id) references cart;

alter table orders
    add constraint orders_person_fk
    foreign key (person_id) references person;

alter table person
    add constraint person_cart_fk
    foreign key (cart_id) references cart;

alter table person
    add constraint person_role_fk
    foreign key (role_id) references role;

alter table products
    add constraint products_cart_fk
    foreign key (cart_id) references cart;

alter table products
    add constraint products_good_fk
    foreign key (good_id) references good;

alter table products
    add constraint product_type_fk
    foreign key (type_id) references product_types;

alter table sales
    add constraint sales_product_fk
    foreign key (product_id) references products;

alter table sales
    add constraint sales_order_fk
    foreign key (order_id) references orders;

