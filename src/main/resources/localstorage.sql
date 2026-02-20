CREATE TABLE public.localstorage
(
    id integer,
    subFolder character varying(2),
    subSubFolder character varying(2),
    PRIMARY KEY (id),
    CONSTRAINT id FOREIGN KEY (id)
        REFERENCES public.pictures (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.localstorage
    OWNER to "Bogos";