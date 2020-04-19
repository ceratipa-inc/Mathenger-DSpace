import axios from 'axios';

export const mathService = {
    transformToLatex
};

function transformToLatex(formula) {
    return axios.post("/math/transform/latex", formula, {
        headers: {'Content-Type': 'text/plain'}
    })
        .then(response => response.data);
}
