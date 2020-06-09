--
-- Name: request; Type: TABLE; Schema: public; Owner: biblioc
--

CREATE TABLE public.request (
    request_id integer NOT NULL,
    alert_date timestamp without time zone,
    request_date timestamp without time zone,
    book_book_id integer,
    member_member_id integer
);

ALTER TABLE public.request OWNER TO biblioc;

--
-- Name: request_request_id_seq; Type: SEQUENCE; Schema: public; Owner: biblioc
--

CREATE SEQUENCE public.request_request_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.request_request_id_seq OWNER TO biblioc;

--
-- Name: request_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: biblioc
--

ALTER SEQUENCE public.request_request_id_seq OWNED BY public.request.request_id;

--
-- Name: request request_id; Type: DEFAULT; Schema: public; Owner: biblioc
--

ALTER TABLE ONLY public.request ALTER COLUMN request_id SET DEFAULT nextval('public.request_request_id_seq'::regclass);

--
-- Name: request_request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: biblioc
--

SELECT pg_catalog.setval('public.request_request_id_seq', 1, false);

--
-- Name: request request_pkey; Type: CONSTRAINT; Schema: public; Owner: biblioc
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT request_pkey PRIMARY KEY (request_id);

--
-- Name: request fkb50h1gvxtptrj2ji21i5ntj4x; Type: FK CONSTRAINT; Schema: public; Owner: biblioc
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkb50h1gvxtptrj2ji21i5ntj4x FOREIGN KEY (book_book_id) REFERENCES public.book(book_id);

--
-- Name: request fkr48ldhu5j143oc84yxuwh4pen; Type: FK CONSTRAINT; Schema: public; Owner: biblioc
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkr48ldhu5j143oc84yxuwh4pen FOREIGN KEY (member_member_id) REFERENCES public.member(member_id);

