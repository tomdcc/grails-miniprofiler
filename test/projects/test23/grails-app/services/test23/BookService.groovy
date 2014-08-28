package test23

class BookService {

	List<Book> findStuff() {
		Book.findAllByTitleLike('foo')
	}

}