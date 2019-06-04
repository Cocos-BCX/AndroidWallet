function _sendPeRequest(serialNumber, params, methodName) {
    let userAgentVal = navigator.userAgent;
    if (userAgentVal.indexOf('Android') > -1 || userAgentVal.indexOf('Adr') > -1) {
        window.DappJsBridge.pushMessage(serialNumber, JSON.stringify(params), methodName)
    } else {
        window.webkit.messageHandlers.pushMessage.postMessage({
            'params': params,
            'serialNumber': serialNumber,
            'methodName': methodName
        })
    }
}

function serialNumberFn() {
    return 'serialNumber' + (new Date().getTime() + parseInt(Math.random() * 100000000000))
}
class ClientFunction {
    constructor() {}
    getAccountBalances(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'getAccountBalances');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    transferAsset(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'transferAsset');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    callContractFunction(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'callContractFunction');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    queryContract(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryContract');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    queryAccountContractData(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryAccountContractData');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    queryAccountInfo(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryAccountInfo');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    getAccountInfo() {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, '', 'getAccountInfo');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    queryNHAssets(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryNHAssets');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                   resolve(JSON.parse(result));
                }
            }
        })
    }
    queryAccountNHAssetOrders(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryAccountNHAssetOrders');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    queryNHAssetOrders(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryNHAssetOrders');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    queryAccountNHAssets(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'queryAccountNHAssets');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    decodeMemo(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'decodeMemo');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    transferNHAsset(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'transferNHAsset');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }
    fillNHAssetOrder(params) {
        const serialNumber = serialNumberFn();
        _sendPeRequest(serialNumber, params, 'fillNHAssetOrder');
        return new Promise((resolve, reject) => {
            window.callbackResult = function (returnSerialNumber, result) {
                if (returnSerialNumber == serialNumber) {
                    resolve(JSON.parse(result));
                }
            }
        })
    }




}
const hookFunction = new ClientFunction();
const checkForExtension = (resolve, tries = 0) => {
    if (tries > 20) return;
    if (window.scatter.isExtension) return resolve(true);
    setTimeout(() => checkForExtension(resolve, tries + 1), 100)
};

class Index {
    constructor() {
        this.isExtension = true
    }

    async isInstalled() {
        return new Promise(resolve => {
            setTimeout(() => {
                resolve(false)
            }, 3000);
            Promise.race([checkForExtension(resolve)])
        })
    }
    async connect(pluginName, options) {
        return new Promise(resolve => {
            if (!pluginName || !pluginName.length) throw new Error('You must specify a name for this connection');
            options = Object.assign({
                initTimeout: 10000,
                linkTimeout: 30000
            }, options);
            setTimeout(() => {
                resolve(false)
            }, options.initTimeout);
            checkForExtension(resolve)
        })
    }
    disconnect() {}
    forgetIdentity() {
        return new Promise((resolve, reject) => {
            this.identity = null;
            resolve(true)
        })
    }
    getIdentityFromPermissions() {
        return new Promise((resolve, reject) => {
            hookFunction.getAccountInfo().then((res) => {
                var account = res.data;
                resolve(account)
            })
        })
    }
    queryAccountBalances(params) {
        return new Promise((resolve, reject) => {
            hookFunction.getAccountBalances(params).then((res) => {
                resolve(res)
            })
        })
    }
    transferAsset(params) {
        return new Promise((resolve, reject) => {
            hookFunction.transferAsset(params).then((res) => {
                resolve(res)
            })
        })
    }
    callContractFunction(params) {
        return new Promise((resolve, reject) => {
            hookFunction.callContractFunction(params).then((res) => {
                resolve(res)
            })
        })
    }
    queryContract(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryContract(params).then((res) => {
                resolve(res)
            })
        })
    }
    queryAccountContractData(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryAccountContractData(params).then((res) => {
                resolve(res)
            })
        })
    }
    queryAccountInfo(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryAccountInfo(params).then((res) => {
                resolve(res)
            })
        })
    }
    getAccountInfo() {
        return new Promise((resolve, reject) => {
            hookFunction.getAccountInfo().then((res) => {
                resolve(res)
            })
        })
    }
    queryNHAssets(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryNHAssets(params).then((res) => {
                resolve(res)
            })
        })
    }
    queryAccountNHAssetOrders(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryAccountNHAssetOrders(params).then((res) => {
                resolve(res)
            })
        })
    }
    queryNHAssetOrders(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryNHAssetOrders(params).then((res) => {
                resolve(res)
            })
        })
    }
    queryAccountNHAssets(params) {
        return new Promise((resolve, reject) => {
            hookFunction.queryAccountNHAssets(params).then((res) => {
                resolve(res)
            })
        })
    }
    decodeMemo(params) {
        return new Promise((resolve, reject) => {
            hookFunction.decodeMemo(params).then((res) => {
                resolve(res)
            })
        })
    }
    transferNHAsset(params) {
        return new Promise((resolve, reject) => {
            hookFunction.transferNHAsset(params).then((res) => {
                resolve(res)
            })
        })
    }
    fillNHAssetOrder(params) {
        return new Promise((resolve, reject) => {
            hookFunction.fillNHAssetOrder(params).then((res) => {
                resolve(res)
            })
        })
    }
}


function inject() {
    window.BcxWeb = new Index();
    BcxWeb.getAccountInfo().then(res=>{
        window.BcxWeb.account_name = res.account_name;
    });
    console.log('Release-V 1.0.3');
    document.dispatchEvent(new CustomEvent('scatterLoaded'));
}
inject();