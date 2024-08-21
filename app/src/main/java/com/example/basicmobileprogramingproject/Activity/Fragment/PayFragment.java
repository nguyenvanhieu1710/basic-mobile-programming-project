package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.CustomPayAdapter;
import com.example.basicmobileprogramingproject.Adapter.PayAdapter;
import com.example.basicmobileprogramingproject.Adapter.ProductAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.CartEntity;
import com.example.basicmobileprogramingproject.Entity.OrderDetailEntity;
import com.example.basicmobileprogramingproject.Entity.OrderEntity;
import com.example.basicmobileprogramingproject.Entity.ProductEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Entity.VoucherEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.CartModel;
import com.example.basicmobileprogramingproject.Model.OrderDetailModel;
import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.Model.VoucherModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class PayFragment extends Fragment {
    View view;
    TextView tvTotalAmount;
    Button btnPay;
    Spinner spinnerVoucher, spinnerStaff;
    RecyclerView recyclerViewProduct;
    PayAdapter payAdapter;
    List<CartModel> cartList;
    ArrayList<VoucherModel> voucherList;
    ArrayList<UserModel> staffList;
    CartEntity cartEntity;
    ProductEntity productEntity;
    VoucherEntity voucherEntity;
    UserEntity userEntity;
    OrderEntity orderEntity;
    OrderDetailEntity orderDetailEntity;
    AccountEntity accountEntity;
    int staffIdHasBeenSelected;
    int voucherIdHasBeenSelected;
    double totalAmount;
    double discountAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pay_fragment, container, false);

//        for (CartModel cart : cartList) {
//            AlertDialogUtils.showInfoDialog(getContext(), "Check product id: " + cart.ProductId);
//            AlertDialogUtils.showInfoDialog(getContext(), "Check user id: " + cart.UserId);
//            AlertDialogUtils.showInfoDialog(getContext(), "Check quantity: " + cart.getQuantity());
//            AlertDialogUtils.showInfoDialog(getContext(), "Check calculated price: " + cart.getCalculatedPrice());
//        }

        // mapping id
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        btnPay = view.findViewById(R.id.btnPay);
        spinnerVoucher = view.findViewById(R.id.spinnerVoucher);
        spinnerStaff = view.findViewById(R.id.spinnerStaff);

        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct);
        payAdapter = new PayAdapter(getContext(), cartList);

        accountEntity = new AccountEntity(getContext());

        cartEntity = new CartEntity(getContext());
        productEntity = new ProductEntity(getContext());

        orderEntity = new OrderEntity(getContext());
        orderDetailEntity = new OrderDetailEntity(getContext());

        voucherEntity = new VoucherEntity(getContext());
        voucherList = voucherEntity.getVoucherList();

        userEntity = new UserEntity(getContext());
        staffList = userEntity.getStaffList();

        uploadDataToSpinner();
        uploadTotalAmount();
        uploadDataToRecyclerView();
        handleEventsClickButton();
        selectItemSpinner();

        return view;
    }

    public void uploadDataToRecyclerView() {
        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(layoutManager);
        recyclerViewProduct.setAdapter(payAdapter);
    }

    public void uploadTotalAmount() {
        double totalAmount = 0;
        for (CartModel cart : cartList) {
            totalAmount += cart.getCalculatedPrice();
        }
        tvTotalAmount.setText("Total Amount: $" + totalAmount);
        this.totalAmount = totalAmount;
    }

    public void getProductsInfo(List<CartModel> cartList) {
        this.cartList = cartList;
    }

    public void handleEventsClickButton() {
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                AccountModel accountModel = accountEntity.getOnlineAccount();
                OrderModel orderModel = new OrderModel();
                orderModel.UserId = accountModel.AccountId;
                orderModel.StaffId = staffIdHasBeenSelected;
                orderModel.OrderStatus = "Wait for confirmation";
                orderModel.TotalAmount = totalAmount;
                if (!orderEntity.insertOrder(orderModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Error when insert order");
                    return;
                }
                // insert order detail
                for (CartModel cart : cartList) {
                    OrderDetailModel orderDetailModel = new OrderDetailModel();
                    orderDetailModel.OrderId = orderEntity.getFinalOrderId();
                    orderDetailModel.ProductId = cart.ProductId;
                    orderDetailModel.Quantity = cart.getQuantity();
                    orderDetailModel.Price = cart.getCalculatedPrice();
                    orderDetailModel.DiscountAmount = discountAmount;
                    orderDetailModel.VoucherId = voucherIdHasBeenSelected;
                    if (!orderDetailEntity.insertOrderDetail(orderDetailModel)) {
                        AlertDialogUtils.showErrorDialog(getContext(), "Error when insert order detail");
                        return;
                    }
                }
                // delete cart
                for (CartModel cart : cartList) {
                    if (!cartEntity.deleteCart(cart)) {
                        AlertDialogUtils.showErrorDialog(getContext(), "Error when delete cart");
                        return;
                    }
                }
                // minus quantity product after pay
                ArrayList<OrderDetailModel> orderDetailList = orderDetailEntity.getOrderDetailList();
                for (OrderDetailModel orderDetail : orderDetailList) {
                    if (!productEntity.minusQuantityAfterPay(orderDetail.ProductId, orderDetail.Quantity)) {
                        AlertDialogUtils.showErrorDialog(getContext(), "Error when minus quantity product");
                        return;
                    }
                }
                AlertDialogUtils.showSuccessDialog(getContext(), "Please come see the staff to receive your goods");
                replaceFragment(new HomeFragment());
            }
        });
    }

    public void uploadDataToSpinner() {
        CustomPayAdapter<VoucherModel> voucherAdapter = new CustomPayAdapter<>(
                getContext(), R.layout.spinner_item_of_voucher, voucherList
        );
        spinnerVoucher.setAdapter(voucherAdapter);

        CustomPayAdapter<UserModel> staffAdapter = new CustomPayAdapter<>(
                getContext(), R.layout.spinner_item_of_staff, staffList
        );
        spinnerStaff.setAdapter(staffAdapter);
    }

    public void selectItemSpinner() {
        spinnerStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserModel selectedUser = (UserModel) parent.getItemAtPosition(position);
                // get id from selectedUser
                AlertDialogUtils.showInfoDialog(getContext(), "Selected Staff: " + selectedUser.UserId);
                staffIdHasBeenSelected = selectedUser.UserId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Hành động khi không có item nào được chọn, có thể không cần thiết
                AlertDialogUtils.showErrorDialog(getContext(), "Please choose staff");
            }
        });

        spinnerVoucher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VoucherModel selectedVoucher = (VoucherModel) parent.getItemAtPosition(position);
                // get id voucher from selectedVoucher
                AlertDialogUtils.showInfoDialog(getContext(), "Selected Voucher: " + selectedVoucher.VoucherId);
                voucherIdHasBeenSelected = selectedVoucher.VoucherId;
                discountAmount = selectedVoucher.Price;
                tvTotalAmount.setText("Total Amount: $" + (totalAmount - discountAmount));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AlertDialogUtils.showErrorDialog(getContext(), "Please choose voucher");
            }
        });

    }

    public boolean validateData() {
        if (totalAmount == 0) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please check total amount");
            return false;
        }
        if (discountAmount == 0) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please check discount amount");
            return false;
        }
        if (totalAmount < discountAmount) {
            AlertDialogUtils.showErrorDialog(getContext(), "Total amount must be greater than discount amount");
            return false;
        }
        if (staffIdHasBeenSelected == 0) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please choose staff");
            return false;
        }
        if (voucherIdHasBeenSelected == 0) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please choose voucher");
            return false;
        }
        if (totalAmount == 0) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please check total amount");
            return false;
        }
//        if (spinnerVoucher.getSelectedItemPosition() == 0) {
//            AlertDialogUtils.showErrorDialog(getContext(), "Please choose voucher");
//            return false;
//        }
//        if (spinnerStaff.getSelectedItemPosition() == 0) {
//            AlertDialogUtils.showErrorDialog(getContext(), "Please choose staff");
//            return false;
//        }
        return true;
    }
    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }
}
