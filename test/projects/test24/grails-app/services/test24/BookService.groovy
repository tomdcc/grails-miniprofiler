package test24

class BookService {

	List<Book> findStuff() {
		Book.findAllByTitleLike('foo')
	}

}