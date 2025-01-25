import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function Layout({ children }) {
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        navigate('/login');
    };
    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            <header className="bg-white shadow-sm">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
                    <h1 className="text-2xl font-bold">LeaveSync</h1>
                    <nav>
                        <div>
                            {token ? (
                                <button onClick={handleLogout} className="w-full px-3 py-2 bg-gray-800 text-white rounded-md shadow-sm hover:bg-gray-700 transition duration-300 ease-in-out">
                                    Logout
                                </button>
                            ) : (
                                <button onClick={() => navigate('/login')} className="w-full px-3 py-2 bg-gray-800 text-white rounded-md shadow-sm hover:bg-gray-700 transition duration-300 ease-in-out">
                                    Login
                                </button>
                            )}
                        </div>
                    </nav>
                </div>
            </header>

            <main className="flex-grow">{children}</main>

            <footer className="bg-white shadow-sm mt-8">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 text-center text-gray-500 text-sm">
                    {new Date().getFullYear()} LeaveSync. All rights reserved.
                </div>
            </footer>
        </div>
    );
}