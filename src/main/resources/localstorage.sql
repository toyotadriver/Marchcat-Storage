CREATE TABLE public.localstorage
(
    id integer,
    folder character varying(6),
    PRIMARY KEY (id),
    CONSTRAINT id FOREIGN KEY (id)
        REFERENCES public.pictures (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.localstorage
    OWNER to "Bogos";