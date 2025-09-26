import React, { useState } from 'react';
import BookList from './components/BookList';
import AddBookForm from './components/AddBookForm';

export default function App() {
  const [currentView, setCurrentView] = useState('form'); // 'form' or 'list'
  const [refreshTrigger, setRefreshTrigger] = useState(0);

  const handleBookAdded = () => {
    setRefreshTrigger(prev => prev + 1);
  };

  const handleNavigateToList = () => {
    setCurrentView('list');
  };

  const handleNavigateToForm = () => {
    setCurrentView('form');
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4 flex justify-center">Book Store</h1>
      
      {currentView === 'form' ? (
        <AddBookForm 
          onAdded={handleBookAdded}
          onNavigateToList={handleNavigateToList}
        />
      ) : (
        <BookList 
          trigger={refreshTrigger}
          onNavigateToForm={handleNavigateToForm}
        />
      )}
    </div>
  );
}
