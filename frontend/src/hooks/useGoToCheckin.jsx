import { useNavigate } from "react-router-dom";

export default function useGoToCheckin() {
    const navigate = useNavigate();

    return (space) => {
        navigate("/checkedin", {
        state: {
            space,            
            openPopup: true, 
        },
        });
    };
}