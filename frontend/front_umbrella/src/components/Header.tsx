import {Each} from "./Each";
import {Bars3Icon} from "@heroicons/react/16/solid";
import {useState} from "react";
import {RegisterForm} from "./RegisterForm";
import {LoginForm} from "./LoginForm";

interface NavigationItem {
    name: string;
    href: string;
}

interface HeaderProps {
  setModalOpen: (isModalOpen: boolean) => void;
}

const navigation = [
    { name: 'Product', href: '#' },
    { name: 'Features', href: '#' },
    { name: 'Contacts', href: '#' },
]

export function Header() {

    const [isModalOpen, setModalOpen] = useState(false);
    const [formType, setFormType] = useState<'login' | 'register'>('login');

    return (
        <>
            {isModalOpen &&
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-4 rounded-lg shadow-md relative sm:mx-auto sm:w-full sm:max-w-md">
                        <button
                            className="absolute top-2 right-2 text-red-600 hover:text-red-800"
                            onClick={() => {
                                setModalOpen(false);
                                setFormType('login');
                            }
                        }
                        > &#10005;
                        </button>
                        {formType === 'login' ? (
                            <LoginForm setFormType={setFormType} />
                        ) : (
                            <RegisterForm setFormType={setFormType} />
                        )}
                    </div>
                </div>
            }
        <header className="absolute inset-x-0 top-0 z-50">
            <nav className="flex items-center justify-between p-6 lg:px-8" aria-label="Global">
                <div className="flex lg:flex-1">
                    <a href="#" className="-m-1.5 p-1.5">
                        <span className="sr-only">Umbrella</span>
                        <img
                            className="h-8 w-auto"
                            src="https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=600"
                            alt=""
                        />
                    </a>
                </div>
                <div className="flex lg:hidden">
                    <button
                        type="button"
                        className="-m-2.5 inline-flex items-center justify-center rounded-md p-2.5 text-gray-700"
                    >
                        <span className="sr-only">Open main menu</span>
                        <Bars3Icon className="h-6 w-6" aria-hidden="true"/>
                    </button>
                </div>
                <div className="hidden lg:flex lg:gap-x-12">
                    <Each of={navigation} render={(item:NavigationItem, index) =>
                        <a className=" text-base font-semibold leading-6 text-gray-700" key={index}>{`${item.name}`}</a>
                    }/>
                </div>
                    <div className="hidden lg:flex lg:flex-1 lg:justify-end">
                        <a href="#" onClick={() => setModalOpen(true)} className="text-sm font-semibold leading-6 text-gray-900">
                            Log in <span >&rarr;</span>
                        </a>
                    </div>
            </nav>
        </header>
            </>
    );
}
