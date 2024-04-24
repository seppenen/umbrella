import React, {ChangeEventHandler, useState} from "react";

interface FormValues {
    email: string;
    password: string;
}
export const useForm = (initialValues: FormValues) => {
    const [values, setValues] = useState(initialValues);

    const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        const { name, value } = e.currentTarget;
        setValues(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    return { values, handleChange };
};
