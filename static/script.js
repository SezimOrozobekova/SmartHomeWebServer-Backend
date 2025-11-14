function loadState() {
    fetch("/state")
        .then(r => r.json())
        .then(data => {
            document.getElementById("lamp").src =
                data.light.on ? "lamp_on.png" : "lamp_off.png";

            document.getElementById("kettle").src =
                data.kettle.on ? "kettle_on.png" : "kettle_off.png";

            document.getElementById("fridge").src =
                data.fridge.on ? "fridge_on.png" : "fridge_off.png";
        });
}

function toggle(device) {
    fetch("/toggle?device=" + device)
        .then(() => loadState());
}

loadState();
