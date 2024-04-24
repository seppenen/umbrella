import React, {useEffect, useRef} from 'react';

import {Header} from "./components/Header";
import {gsap} from "gsap"


function App() {

    const ref = useRef(null);

    useEffect(() => {
        gsap.from(ref.current, { duration: 1, opacity: 0 });
    }, []);

  return (
      <>
          <Header />
          <div ref={ref} className="flex flex-col items-center justify-center min-h-screen text-center py-20 bg-white-500">
              <h1  className="text-6xl font-bold text-gray-700 mb-4">Hello, Umbrella!</h1>
              <p  className="text-2xl text-gray-700 mb-4">Finances should be simple.</p>
              <a className="font-custom py-3 px-10 bg-white hover:bg-gray-200 text-blue-500 font-semibold rounded-lg" href="#" rel="noopener noreferrer">
                  Join us
              </a>
          </div>
      </>
  );
}

export default App;
