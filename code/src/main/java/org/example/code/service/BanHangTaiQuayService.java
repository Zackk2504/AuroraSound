package org.example.code.service;

import org.example.code.model.HoaDon;
import org.example.code.model.HoaDonChiTiet;
import org.example.code.model.SanPhamChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BanHangTaiQuayService {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    public List<SanPhamChiTiet> DanhSachSanPham() {
        return sanPhamChiTietService.getAllSanPhamChiTiet();
    }
    public List<HoaDonChiTiet> DanhSachHoaDonChiTiet(Integer idhd) {
        return hoaDonChiTietService.getListHoaDonChiTietByIdHoaDon(idhd);
    }

    public Integer ThemHoaDonChiTiet(Integer idsp,Integer soLuong, Integer idHoaDon,String nhaptay) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietService.getSanPhamChiTietById(idsp)
                .orElseThrow(() -> new RuntimeException("SanPhamChiTiet not found"));
        if (idHoaDon == 0) {
            if (sanPhamChiTiet.getSoLuongTon() < 1) {
                throw new RuntimeException("So luong san pham khong du");
            }
            HoaDon hoaDon = new HoaDon();
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setIdHoadon(hoaDon);
            hoaDonChiTiet.setIdSanphamchitiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(1);
            hoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - 1);
            hoaDonService.addAndEdit(hoaDon);
            hoaDonChiTietService.addAndEdit(hoaDonChiTiet);
            sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
            return hoaDon.getId();
        }else {
            Optional<HoaDon> hoaDonOptional = hoaDonService.getHoaDonById(idHoaDon);
            if (hoaDonOptional.isPresent()) {
                HoaDon hoaDon = hoaDonOptional.get();
                Optional<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietService.getHoaDonChiTietByIdSanPhamAndIdHoaDon(idsp, idHoaDon);
                if (hoaDonChiTiet.isPresent()) {
                    HoaDonChiTiet existingHoaDonChiTiet = hoaDonChiTiet.get();
                    int oldSoLuong = existingHoaDonChiTiet.getSoLuong();
                    int newSoLuong = oldSoLuong + soLuong;
                    int chenhLech = newSoLuong - oldSoLuong;
                    System.out.println("Chenh lech: " + chenhLech);

                    if (nhaptay.equals("true")) {
                        if (soLuong <= 0) {
                            hoaDonChiTietService.delete(existingHoaDonChiTiet.getId());
                        } else {
                            if (sanPhamChiTiet.getSoLuongTon() < chenhLech) {
                                throw new RuntimeException("Không đủ hàng tồn");
                            }
                            chenhLech = soLuong - oldSoLuong;
                            existingHoaDonChiTiet.setSoLuong(soLuong);
                            sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - chenhLech);
                            System.out.println("so luong sp trong hd " + existingHoaDonChiTiet.getSoLuong());
                            System.out.println("chenh lech " + chenhLech);
                            System.out.println("cai tru vao so luong ton " + (sanPhamChiTiet.getSoLuongTon() - chenhLech));
                            hoaDonChiTietService.addAndEdit(existingHoaDonChiTiet);
                            sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                        }
                    }else{
                    if (newSoLuong <= 0) {
                        hoaDonChiTietService.delete(existingHoaDonChiTiet.getId());
                    } else {
                        if (sanPhamChiTiet.getSoLuongTon() < chenhLech) {
                            throw new RuntimeException("So luong san pham khong du");
                        }
                        existingHoaDonChiTiet.setSoLuong(newSoLuong);
                        sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - chenhLech);
                        hoaDonChiTietService.addAndEdit(existingHoaDonChiTiet);
                        sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                    }
                    }
                } else {
                    if (sanPhamChiTiet.getSoLuongTon() < 1) {
                        throw new RuntimeException("So luong san pham khong du");
                    }
                    HoaDonChiTiet newHoaDonChiTiet = new HoaDonChiTiet();
                    newHoaDonChiTiet.setIdHoadon(hoaDon);
                    newHoaDonChiTiet.setIdSanphamchitiet(sanPhamChiTiet);
                    newHoaDonChiTiet.setSoLuong(1);
                    newHoaDonChiTiet.setDonGia(sanPhamChiTiet.getDonGia());
                    sanPhamChiTiet.setSoLuongTon(sanPhamChiTiet.getSoLuongTon() - 1);
                    hoaDonChiTietService.addAndEdit(newHoaDonChiTiet);
                    sanPhamChiTietService.addAndEdit(sanPhamChiTiet);
                }

                return hoaDon.getId();
            } else {
                throw new RuntimeException("Hoa Don not found");
            }
        }

    }




}
