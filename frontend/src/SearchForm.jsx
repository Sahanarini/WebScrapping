// // src/ScrapeForm.js
// import React, { useState } from 'react';
// import axios from 'axios';

// const ScrapeForm = () => {
//     const [searchValue, setSearchValue] = useState('');
//     const [message, setMessage] = useState('');

//     const handleSubmit = async (event) => {
//         event.preventDefault();
//         if (!searchValue) {
//             setMessage('Please enter a search value.');
//             return;
//         }

//         try {
//             const response = await axios.post('http://localhost:8080/api/scrape', searchValue, {
//                 headers: {
//                     'Content-Type': 'text/plain',
//                 },
//             });
//             setMessage(response.data);
//         } catch (error) {
//             console.error('Error during scraping:', error);
//             setMessage('An error occurred while scraping. Please try again.');
//         }
//     };

//     return (
//         <div style={{ padding: '20px' }}>
//             <h2>Scrape Amazon</h2>
//             <form onSubmit={handleSubmit}>
//                 <input
//                     type="text"
//                     value={searchValue}
//                     onChange={(e) => setSearchValue(e.target.value)}
//                     placeholder="Enter search value"
//                     required
//                     style={{ marginRight: '10px' }}
//                 />
//                 <button type="submit">Scrape</button>
//             </form>
//             {message && <p>{message}</p>}
//         </div>
//     );
// };

// export default ScrapeForm;


import React, { useState } from 'react';
import axios from 'axios';

const ScrapeForm = () => {
    const [searchValue, setSearchValue] = useState('');
    const [message, setMessage] = useState('');
    const [results, setResults] = useState([]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!searchValue) {
            setMessage('Please enter a search value.');
            return;
        }

        try {
            const response = await axios.post('http://localhost:8080/api/scrape', searchValue, {
                headers: {
                    'Content-Type': 'text/plain',
                },
            });
            setMessage(response.data);
            fetchResults(searchValue); // Fetch results after scraping
        } catch (error) {
            console.error('Error during scraping:', error);
            setMessage('An error occurred while scraping. Please try again.');
        }
    };

    const fetchResults = async (searchValue) => {
        try {
            const response = await axios.get('http://localhost:8080/api/scrape/results', {
                params: { searchValue }
            });
            setResults(response.data);
        } catch (error) {
            console.error('Error fetching results:', error);
        }
    };

    return (
        <div style={{ padding: '20px' }}>
            <h2>Scrape Amazon</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    value={searchValue}
                    onChange={(e) => setSearchValue(e.target.value)}
                    placeholder="Enter search value"
                    required
                    style={{ marginRight: '10px' }}
                />
                <button type="submit">Scrape</button>
            </form>
            {message && <p>{message}</p>}
            {results.length > 0 && (
                <div>
                    <h3>Scraped Results:</h3>
                    <ul>
                        {results.map((result, index) => (
                            <li key={index}>{result}</li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default ScrapeForm;
