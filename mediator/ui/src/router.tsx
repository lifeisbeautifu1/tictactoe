import {
  createBrowserRouter,
} from "react-router-dom";

import MainPage from "./pages/MainPage/MainPage";
import PlaygroundPage from "./pages/PlaygroundPage/PlaygroundPage";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <MainPage />,
  },
  {
    path: "/games/:sessionId",
    element: <PlaygroundPage />,
  }
]);
