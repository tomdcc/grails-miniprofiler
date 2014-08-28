package test22

class BookService {

	List<Book> findStuff() {
		Book.findAllByTitleLike('foo')
	}

}