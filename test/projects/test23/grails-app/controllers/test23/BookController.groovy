package test23

class BookController {

    static scaffold = Book

	def bookService

	def list() {
		bookService.findStuff()
		Book.findAll()
		render view: '/index'
	}
}
