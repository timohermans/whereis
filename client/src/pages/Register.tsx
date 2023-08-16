import { ChangeEvent, FormEvent, useState } from "react";
import { useDebounceCallback, useLocalStorage } from "../lib/hooks";
import { User } from "../lib/types";
import { router } from "../lib/router";

interface SignUp {
  email: string;
  password: string;
  username: string;
  firstname: string;
  lastname: string;
}

export default function Register() {
  const [form, setForm] = useState<SignUp>({
    email: "",
    username: "",
    firstname: "",
    lastname: "",
    password: "",
  });
  const [message, setMessage] = useState("");
  const [, setUser] = useLocalStorage<User>("user", null);
  const [hasPasswordCheckFailed, setHasPasswordCheckFailed] = useState(false);

  async function handleSumbit(event: FormEvent) {
    event.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/auth/sign-up", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(form),
      });

      // TODO: test existing email
      if (response.status !== 201) {
        setMessage("Something unexpected went wrong. Oh noes 😱");
      } else {
        router.navigate({ to: "/log-in" });
      }
    } catch (error) {
      setMessage(
        "Something unexpected went wrong. I think the server might be broken. Oh noes 😱",
      );
    }
  }

  function handleChange(event: ChangeEvent<HTMLInputElement>) {
    const key = event.target.name;
    const value = event.target.value;
    setForm({
      ...form,
      [key]: value,
    });
  }

  const doPasswordCheck = (event: ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    if (form.password && form.password !== value) {
      setHasPasswordCheckFailed(true);
    } else {
      setHasPasswordCheckFailed(false);
    }
  };

  const [handlePasswordCheck, tryCancelPasswordCheck] =
    useDebounceCallback(doPasswordCheck);

  return (
    <>
      <div className="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
        <div className="sm:mx-auto sm:w-full sm:max-w-sm">
          <h2 className="mt-10 text-center text-2xl font-bold leading-9 tracking-tight text-gray-900">
            Sign in to your account
          </h2>
        </div>

        <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
          <form className="space-y-6" onSubmit={handleSumbit}>
            <div>
              <label
                htmlFor="email"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Email address
              </label>
              <div className="mt-2">
                <input
                  id="email"
                  name="email"
                  type="email"
                  onChange={handleChange}
                  required
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <label
                htmlFor="username"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Username
              </label>
              <div className="mt-2">
                <input
                  id="username"
                  name="username"
                  type="username"
                  onChange={handleChange}
                  required
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <label
                htmlFor="firstname"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Firstname
              </label>
              <div className="mt-2">
                <input
                  id="firstname"
                  name="firstname"
                  type="firstname"
                  onChange={handleChange}
                  required
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <label
                htmlFor="lastname"
                className="block text-sm font-medium leading-6 text-gray-900"
              >
                Lastname
              </label>
              <div className="mt-2">
                <input
                  id="lastname"
                  name="lastname"
                  type="lastname"
                  onChange={handleChange}
                  required
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="password"
                  className="block text-sm font-medium leading-6 text-gray-900"
                >
                  Password
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="password"
                  name="password"
                  type="password"
                  required
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  onChange={handleChange}
                />
              </div>
            </div>

            <div>
              <div className="flex items-center justify-between">
                <label
                  htmlFor="password"
                  className="block text-sm font-medium leading-6 text-gray-900"
                >
                  Repeat password please
                </label>
              </div>
              <div className="mt-2">
                <input
                  id="passwordCheck"
                  type="password"
                  required
                  className="block w-full rounded-md border-0 px-1.5 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  onChange={handlePasswordCheck}
                  onBlur={(e) => {
                    tryCancelPasswordCheck();
                    doPasswordCheck(e);
                  }}
                />
                <br />
                {hasPasswordCheckFailed && (
                  <span className="text-red-500">
                    Yeah my boy, the passwords do not match 🤦🏻
                  </span>
                )}
              </div>
            </div>
            <div className="flex flex-col">
              <button
                type="submit"
                className="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm font-semibold leading-6 text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
              >
                Sign up
              </button>
            </div>
            {message && (
              <div className="mt-6 rounded-md bg-red-200 p-1.5 text-red-500">
                {message}
              </div>
            )}
          </form>
        </div>
      </div>
    </>
  );
}
