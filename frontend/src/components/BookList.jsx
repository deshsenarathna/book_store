// src/components/BookList.jsx
import { useEffect, useState } from 'react';
import { getBooks, deleteBook, updateBook } from '../services/bookService'; // Using mock service for testing

export default function BookList({ trigger, onNavigateToForm }) {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [editingBook, setEditingBook] = useState(null);
  const [editTitle, setEditTitle] = useState('');
  const [editAuthor, setEditAuthor] = useState('');
  const [editPrice, setEditPrice] = useState('');

  // Load from backend
  const loadBooks = async () => {
    setLoading(true);
    setError('');
    try {
      console.log('Attempting to fetch books...');
      const res = await getBooks();
      console.log('Books fetched successfully:', res.data);
      setBooks(res.data || []); // ✅ actual data from backend
    } catch (err) {
      console.error('Error loading books:', err);
      
      // Determine the specific error type
      let errorMessage = 'Failed to load books from API. ';
      if (err.code === 'ERR_NETWORK' || err.message.includes('Network Error')) {
        errorMessage += 'Backend server appears to be offline. Please start the backend server on http://localhost:8080';
      } else if (err.response) {
        errorMessage += `Server responded with error ${err.response.status}: ${err.response.data?.message || err.response.statusText}`;
      } else {
        errorMessage += `${err.message}. Please check if the backend server is running.`;
      }
      
      setError(errorMessage);
      setBooks([]); // Set empty array instead of undefined dummyBooks
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadBooks(); }, [trigger]);

  // Delete from backend
  const handleDelete = async (id) => {
    if (!window.confirm('Delete this book?')) return;
    try {
      await deleteBook(id);
      loadBooks();
    } catch (err) {
      // fallback: remove locally
      setBooks(books.filter(book => book.id !== id));
    }
  };

  // Start editing
  const startEdit = (book) => {
    setEditingBook(book.id);
    setEditTitle(book.title);
    setEditAuthor(book.author);
    setEditPrice(book.price);
  };

  const cancelEdit = () => {
    setEditingBook(null);
    setEditTitle('');
    setEditAuthor('');
    setEditPrice('');
  };

  // Update book
  const handleUpdate = async (id) => {
    const bookToUpdate = books.find(b => b.id === id);
    try {
      await updateBook(id, {
        title: editTitle,
        author: editAuthor,
        price: parseFloat(editPrice),
        isbn: bookToUpdate.isbn,
        stock: bookToUpdate.stock
      });
      cancelEdit();
      loadBooks();
    } catch (err) {
      // fallback: update locally
      setBooks(books.map(book =>
        book.id === id
          ? { ...book, title: editTitle, author: editAuthor, price: parseFloat(editPrice) }
          : book
      ));
      cancelEdit();
    }
  };

  if (loading) return <div className="text-center mt-6">Loading books...</div>;

  return (
    <div className="max-w-3xl mx-auto mt-6">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-semibold text-gray-800">Book List</h2>
        <button
          onClick={onNavigateToForm}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition duration-200 font-medium"
        >
          Add New Book
        </button>
      </div>

      {error && <div className="text-red-600 text-center mb-4">{error}</div>}

      {books.length === 0 && !loading ? (
        <div className="text-center text-gray-500 py-8">
          No books found. Add some books to get started!
        </div>
      ) : (
        <div className="bg-white shadow-lg rounded-2xl p-4 divide-y divide-gray-200">
          {books.map((b) => (
          <div key={b.id} className="py-4 flex flex-col sm:flex-row sm:items-center sm:justify-between">
            {editingBook === b.id ? (
              <div className="w-full sm:flex sm:items-center sm:gap-2">
                <input
                  className="border p-2 rounded w-full sm:w-1/3 mb-2 sm:mb-0"
                  value={editTitle}
                  onChange={(e) => setEditTitle(e.target.value)}
                  placeholder="Title"
                />
                <input
                  className="border p-2 rounded w-full sm:w-1/3 mb-2 sm:mb-0"
                  value={editAuthor}
                  onChange={(e) => setEditAuthor(e.target.value)}
                  placeholder="Author"
                />
                <input
                  type="number"
                  className="border p-2 rounded w-full sm:w-1/6 mb-2 sm:mb-0"
                  value={editPrice}
                  onChange={(e) => setEditPrice(e.target.value)}
                  placeholder="Price"
                />
                <div className="flex gap-2 mt-2 sm:mt-0">
                  <button
                    onClick={() => handleUpdate(b.id)}
                    className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                  >
                    Save
                  </button>
                  <button
                    onClick={cancelEdit}
                    className="bg-gray-500 text-white px-3 py-1 rounded hover:bg-gray-600"
                  >
                    Cancel
                  </button>
                </div>
              </div>
            ) : (
              <>
                <div>
                  <div className="font-semibold text-lg">{b.title}</div>
                  <div className="text-gray-600 text-sm">{b.author}</div>
                  <div className="text-gray-800 text-sm">Price: ${b.price}</div>
                  <div className="text-gray-800 text-sm">ISBN: {b.isbn}</div>
                  <div className="text-gray-800 text-sm">Stock: {b.stock}</div>
                </div>
                <div className="flex gap-2 mt-2 sm:mt-0">
                  <button
                    onClick={() => alert(`Viewing book:\n${b.title}\nAuthor: ${b.author}\nPrice: ${b.price}`)}
                    className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
                  >
                    View
                  </button>
                  <button
                    onClick={() => startEdit(b)}
                    className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                  >
                    Edit
                  </button>
                  <button
                    id={`delete-${b.id}`}
                    onClick={() => handleDelete(b.id)}
                    className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                  >
                    Delete
                  </button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>
      )}
    </div>
  );
}
